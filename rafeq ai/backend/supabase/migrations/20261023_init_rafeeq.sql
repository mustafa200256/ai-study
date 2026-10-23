-- ==============================================================================
-- رفيق الطالب العراقي AI — Rafeeq Student AI
-- Database Schema & Security Policies (Supabase / PostgreSQL)
-- المطور وصاحب المشروع: Mustafa Hussein — مصطفى حسين
-- ==============================================================================

-- 1. Create Enums
DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('student', 'admin', 'super_admin');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE payment_status AS ENUM ('pending', 'approved', 'rejected');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE subscription_status AS ENUM ('active', 'expired', 'canceled');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- 2. Profiles Table (Linked to auth.users)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    email TEXT UNIQUE NOT NULL,
    full_name TEXT NOT NULL DEFAULT 'طالب عراقي',
    phone TEXT,
    stage TEXT DEFAULT 'السادس الإعدادي',
    branch TEXT DEFAULT 'العلمي',
    role user_role DEFAULT 'student',
    is_banned BOOLEAN DEFAULT false,
    questions_asked_today INT DEFAULT 0,
    last_question_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Subscriptions Table
CREATE TABLE IF NOT EXISTS public.subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    plan_name TEXT NOT NULL DEFAULT 'باقة التفوق الذهبية',
    status subscription_status DEFAULT 'active',
    starts_at TIMESTAMPTZ DEFAULT NOW(),
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. Payment Requests Table (Manual Wallet Transfers)
CREATE TABLE IF NOT EXISTS public.payment_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    plan_name TEXT NOT NULL,
    amount_iqd NUMERIC NOT NULL,
    wallet_type TEXT NOT NULL, -- 'SuperQi', 'ZainCash', 'FIB', etc.
    sender_phone TEXT NOT NULL,
    transaction_ref TEXT NOT NULL UNIQUE,
    status payment_status DEFAULT 'pending',
    rejection_reason TEXT,
    approved_by UUID REFERENCES public.profiles(id),
    approved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 5. Wallet Settings Table (Configured from Admin Panel)
CREATE TABLE IF NOT EXISTS public.wallet_settings (
    id INT PRIMARY KEY DEFAULT 1,
    superqi_phone TEXT DEFAULT '07701234567',
    zaincash_phone TEXT DEFAULT '07801234567',
    fib_account TEXT DEFAULT 'FIB-992384',
    transfer_instructions TEXT DEFAULT 'يرجى تحويل مبلغ 10,000 د.ع وكتابة رقم المعاملة في التطبيق لتفعيل الحساب.',
    standard_plan_price NUMERIC DEFAULT 10000,
    term_plan_price NUMERIC DEFAULT 25000,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    updated_by UUID REFERENCES public.profiles(id)
);

-- 6. AI Settings Table
CREATE TABLE IF NOT EXISTS public.ai_settings (
    id INT PRIMARY KEY DEFAULT 1,
    is_enabled BOOLEAN DEFAULT true,
    provider TEXT DEFAULT 'groq', -- 'groq' or 'openai'
    model_name TEXT DEFAULT 'llama-3.3-70b-versatile',
    daily_questions_limit_free INT DEFAULT 5,
    daily_questions_limit_pro INT DEFAULT 100,
    system_prompt TEXT DEFAULT 'أنت المدرس الذكي في تطبيق رفيق الطالب العراقي AI، متخصص في شرح المنهج العراقي (خصوصاً السادس الإعدادي والصفوف المنتهية). اشرح المسائل خطوة بخطوة باللغة العربية بأسلوب مبسط وممتع مع أمثلة وزارية موثوقة.',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 7. Curriculum Subjects (Dynamic Content Management)
CREATE TABLE IF NOT EXISTS public.curriculum_subjects (
    id TEXT PRIMARY KEY, -- 'math', 'physics', 'chemistry', etc.
    stage TEXT NOT NULL DEFAULT 'السادس الإعدادي',
    branch TEXT NOT NULL DEFAULT 'العلمي',
    title_ar TEXT NOT NULL,
    description_ar TEXT,
    icon_name TEXT DEFAULT 'calculate',
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 8. Curriculum Chapters
CREATE TABLE IF NOT EXISTS public.curriculum_chapters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject_id TEXT NOT NULL REFERENCES public.curriculum_subjects(id) ON DELETE CASCADE,
    chapter_number INT NOT NULL,
    title_ar TEXT NOT NULL,
    ministerial_grade_weight TEXT,
    summary_text TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 9. Dynamic Quiz Questions
CREATE TABLE IF NOT EXISTS public.quiz_questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject_id TEXT NOT NULL REFERENCES public.curriculum_subjects(id) ON DELETE CASCADE,
    chapter_id UUID REFERENCES public.curriculum_chapters(id) ON DELETE SET NULL,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_index INT NOT NULL CHECK (correct_index BETWEEN 0 AND 3),
    explanation_text TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 10. App Settings (Welcome banner, Announcements, Maintenance)
CREATE TABLE IF NOT EXISTS public.app_settings (
    id INT PRIMARY KEY DEFAULT 1,
    welcome_message TEXT DEFAULT 'أهلاً بك يا بطل في رفيق الطالب العراقي AI!',
    announcement_text TEXT DEFAULT 'تم تحديث أسئلة المراجعة المركزة للعام الدراسي الحالي.',
    maintenance_mode BOOLEAN DEFAULT false,
    contact_whatsapp TEXT DEFAULT '+9647700000000',
    contact_telegram TEXT DEFAULT '@RafeeqIqSupport',
    developer_name TEXT DEFAULT 'Mustafa Hussein — مصطفى حسين',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 11. Admin Audit Logs
CREATE TABLE IF NOT EXISTS public.admin_audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    admin_id UUID REFERENCES public.profiles(id),
    admin_email TEXT,
    action_type TEXT NOT NULL, -- 'APPROVE_PAYMENT', 'REJECT_PAYMENT', 'UPDATE_WALLET', 'EDIT_CONTENT', etc.
    details JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ==============================================================================
-- ROW LEVEL SECURITY (RLS) & POLICIES
-- ==============================================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.subscriptions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.payment_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.wallet_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.curriculum_subjects ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.curriculum_chapters ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.app_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.admin_audit_logs ENABLE ROW LEVEL SECURITY;

-- Helper Function to check if current user is Admin or Super Admin
CREATE OR REPLACE FUNCTION public.is_admin()
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM public.profiles
        WHERE id = auth.uid() AND role IN ('admin', 'super_admin') AND is_banned = false
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Profiles Policies
CREATE POLICY "Users can read own profile" ON public.profiles
    FOR SELECT USING (auth.uid() = id OR public.is_admin());

CREATE POLICY "Users can update own non-sensitive profile" ON public.profiles
    FOR UPDATE USING (auth.uid() = id)
    WITH CHECK (auth.uid() = id AND role = (SELECT role FROM public.profiles WHERE id = auth.uid()));

CREATE POLICY "Admins have full access to profiles" ON public.profiles
    FOR ALL USING (public.is_admin());

-- Subscriptions Policies
CREATE POLICY "Users can read own subscription" ON public.subscriptions
    FOR SELECT USING (auth.uid() = user_id OR public.is_admin());

CREATE POLICY "Admins manage subscriptions" ON public.subscriptions
    FOR ALL USING (public.is_admin());

-- Payment Requests Policies
CREATE POLICY "Students can view own payment requests" ON public.payment_requests
    FOR SELECT USING (auth.uid() = user_id OR public.is_admin());

CREATE POLICY "Students can insert payment request" ON public.payment_requests
    FOR INSERT WITH CHECK (auth.uid() = user_id AND status = 'pending');

CREATE POLICY "Admins can update and view all payment requests" ON public.payment_requests
    FOR ALL USING (public.is_admin());

-- Public / Student readable tables
CREATE POLICY "Public read for wallet settings" ON public.wallet_settings
    FOR SELECT USING (true);
CREATE POLICY "Admin write for wallet settings" ON public.wallet_settings
    FOR ALL USING (public.is_admin());

CREATE POLICY "Public read for curriculum subjects" ON public.curriculum_subjects
    FOR SELECT USING (is_active = true OR public.is_admin());
CREATE POLICY "Admin write for curriculum subjects" ON public.curriculum_subjects
    FOR ALL USING (public.is_admin());

CREATE POLICY "Public read for curriculum chapters" ON public.curriculum_chapters
    FOR SELECT USING (true);
CREATE POLICY "Admin write for curriculum chapters" ON public.curriculum_chapters
    FOR ALL USING (public.is_admin());

CREATE POLICY "Public read for quiz questions" ON public.quiz_questions
    FOR SELECT USING (true);
CREATE POLICY "Admin write for quiz questions" ON public.quiz_questions
    FOR ALL USING (public.is_admin());

CREATE POLICY "Public read for app settings" ON public.app_settings
    FOR SELECT USING (true);
CREATE POLICY "Admin write for app settings" ON public.app_settings
    FOR ALL USING (public.is_admin());

CREATE POLICY "Admins view audit logs" ON public.admin_audit_logs
    FOR ALL USING (public.is_admin());

-- ==============================================================================
-- STORED PROCEDURES & SECURE ADMIN BOOTSTRAP
-- ==============================================================================

-- 1. Grant Super Admin securely by email
CREATE OR REPLACE FUNCTION public.grant_super_admin(target_email TEXT)
RETURNS TEXT AS $$
DECLARE
    target_id UUID;
BEGIN
    SELECT id INTO target_id FROM auth.users WHERE email = target_email;
    IF target_id IS NULL THEN
        RETURN 'خطأ: لم يتم العثور على مستخدم بهذا البريد الإلكتروني في auth.users. يرجى إنشاء الحساب أولاً.';
    END IF;

    INSERT INTO public.profiles (id, email, full_name, role)
    VALUES (target_id, target_email, 'المدير العام (Super Admin)', 'super_admin')
    ON CONFLICT (id) DO UPDATE
    SET role = 'super_admin', is_banned = false;

    RETURN 'تم ترقية المستخدم ' || target_email || ' إلى Super Admin بنجاح!';
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- 2. Approve Payment Request & Activate Subscription Atomically
CREATE OR REPLACE FUNCTION public.approve_payment_request(
    p_request_id UUID,
    p_duration_days INT DEFAULT 90
)
RETURNS JSONB AS $$
DECLARE
    v_req RECORD;
    v_admin_email TEXT;
BEGIN
    IF NOT public.is_admin() THEN
        RAISE EXCEPTION 'غير مصرح: يجب أن تكون مديراً لتنفيذ هذا الإجراء';
    END IF;

    SELECT * INTO v_req FROM public.payment_requests WHERE id = p_request_id FOR UPDATE;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'طلب الدفع غير موجود';
    END IF;

    IF v_req.status = 'approved' THEN
        RETURN jsonb_build_object('success', false, 'message', 'تمت الموافقة على هذا الطلب مسبقاً');
    END IF;

    -- Update request status
    UPDATE public.payment_requests
    SET status = 'approved',
        approved_by = auth.uid(),
        approved_at = NOW()
    WHERE id = p_request_id;

    -- Create or extend subscription
    INSERT INTO public.subscriptions (user_id, plan_name, status, starts_at, expires_at)
    VALUES (v_req.user_id, v_req.plan_name, 'active', NOW(), NOW() + (p_duration_days || ' days')::INTERVAL);

    -- Log action
    SELECT email INTO v_admin_email FROM auth.users WHERE id = auth.uid();
    INSERT INTO public.admin_audit_logs (admin_id, admin_email, action_type, details)
    VALUES (
        auth.uid(),
        v_admin_email,
        'APPROVE_PAYMENT',
        jsonb_build_object(
            'request_id', p_request_id,
            'user_id', v_req.user_id,
            'amount', v_req.amount_iqd,
            'transaction_ref', v_req.transaction_ref
        )
    );

    RETURN jsonb_build_object('success', true, 'message', 'تمت الموافقة وتفعيل الاشتراك بنجاح');
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Insert initial singletons
INSERT INTO public.wallet_settings (id) VALUES (1) ON CONFLICT DO NOTHING;
INSERT INTO public.ai_settings (id) VALUES (1) ON CONFLICT DO NOTHING;
INSERT INTO public.app_settings (id) VALUES (1) ON CONFLICT DO NOTHING;
