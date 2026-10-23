// ==============================================================================
// رفيق الطالب العراقي AI — Backend Server (Node.js / Express)
// خادم وسيط آمن لحماية مفاتيح API وإدارة المدرس الذكي
// صاحب المشروع والمطور: Mustafa Hussein — مصطفى حسين
// ==============================================================================

import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

// Health Check
app.get('/api/health', (req, res) => {
  const hasGroqKey = Boolean(process.env.GROQ_API_KEY && process.env.GROQ_API_KEY.trim() !== '');
  res.json({
    status: 'online',
    appName: 'رفيق الطالب العراقي AI',
    developer: 'Mustafa Hussein — مصطفى حسين',
    aiProvider: 'Groq (LLaMA 3.3 70B)',
    hasApiKeyConfigured: hasGroqKey,
    timestamp: new Date().toISOString()
  });
});

// AI Chat Endpoint
app.post('/api/chat', async (req, res) => {
  const groqApiKey = process.env.GROQ_API_KEY;

  if (!groqApiKey || groqApiKey.trim() === '' || groqApiKey === 'YOUR_GROQ_API_KEY_HERE') {
    return res.status(503).json({
      error: 'GROQ_API_KEY_NOT_CONFIGURED',
      message: 'تنبيه: لم يتم ضبط مفتاح GROQ_API_KEY في ملف .env أو متغيرات بيئة الخادم. يرجى إضافة المفتاح لتفعيل ردود المدرس الذكي الحية.'
    });
  }

  const { question, messages = [] } = req.body;

  if (!question && messages.length === 0) {
    return res.status(400).json({ error: 'INVALID_REQUEST', message: 'السؤال مطلوب' });
  }

  const systemPrompt = {
    role: 'system',
    content: `أنت المدرس الذكي في تطبيق «رفيق الطالب العراقي AI»، إشراف وتطوير: مصطفى حسين (Mustafa Hussein).
أنت معلم وخبير تربوي للمناهج العراقية الوزارية (خصوصاً السادس الإعدادي العلمي والأدبي، والثالث المتوسط).
إرشاداتك:
1. اشرح المسائل والقوانين خطوة بخطوة باللغة العربية الواضحة.
2. استخدم مصطلحات المنهج العراقي المعتمد.
3. عند حل أي مسألة، اذكر القانون أولاً ثم المعطيات ثم خطوات التعويض والناتج مع الوحدات.
4. شجع الطالب بكلمات طيبة وداعمة (يا بطل، دكتور/مهندس المستقبل).
5. إذا لم تكن متأكداً من جواب وزاري محدد، وضّح ذلك بصدق وانصح الطالب بمراجعة كتاب وزارة التربية.`
  };

  const payloadMessages = [systemPrompt];
  for (const m of messages) {
    if (m.role && m.content) {
      payloadMessages.push({
        role: m.role === 'user' ? 'user' : 'assistant',
        content: m.content
      });
    }
  }

  if (question && (!messages.length || messages[messages.length - 1].content !== question)) {
    payloadMessages.push({ role: 'user', content: question });
  }

  try {
    const response = await fetch('https://api.groq.com/openai/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${groqApiKey}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        model: process.env.AI_MODEL || 'llama-3.3-70b-versatile',
        messages: payloadMessages,
        temperature: 0.6,
        max_tokens: 1500
      })
    });

    if (!response.ok) {
      const errText = await response.text();
      return res.status(502).json({
        error: 'AI_PROVIDER_ERROR',
        message: `خطأ من مزود الذكاء الاصطناعي: ${response.status}`,
        details: errText
      });
    }

    const data = await response.json();
    const reply = data.choices?.[0]?.message?.content || 'لم يتم استلام رد من المدرس الذكي.';

    res.json({ reply });
  } catch (error) {
    res.status(500).json({
      error: 'SERVER_ERROR',
      message: error.message || 'حدث خطأ في الخادم أثناء معالجة الطلب'
    });
  }
});

app.listen(PORT, () => {
  console.log(`====================================================`);
  console.log(`  رفيق الطالب العراقي AI — Backend Server`);
  console.log(`  صاحب المشروع والمطور: Mustafa Hussein`);
  console.log(`  الخادم يعمل على المنفذ: http://localhost:${PORT}`);
  console.log(`====================================================`);
});
